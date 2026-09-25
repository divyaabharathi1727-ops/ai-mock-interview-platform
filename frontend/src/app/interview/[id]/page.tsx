"use client";

import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useMemo, useState } from "react";
import { ProtectedRoute } from "@/components/ProtectedRoute";
import { ApiError, finishInterview, getInterview, getInterviewQuestions, startInterview, submitAnswer } from "@/lib/api";
import type { AnswerRequest, Interview, InterviewQuestion, InterviewQuestionType } from "@/types/auth";

type DraftAnswer = { text?: string; option?: string; options?: string[] };
const draftKey = (id: number) => `interview-draft-${id}`;

function SessionContent() {
  const params = useParams<{ id: string }>();
  const router = useRouter();
  const interviewId = Number(params.id);
  const [interview, setInterview] = useState<Interview | null>(null);
  const [questions, setQuestions] = useState<InterviewQuestion[]>([]);
  const [answers, setAnswers] = useState<Record<number, DraftAnswer>>({});
  const [currentIndex, setCurrentIndex] = useState(0);
  const [elapsedSeconds, setElapsedSeconds] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!Number.isInteger(interviewId)) {
      setError("That interview could not be found.");
      setIsLoading(false);
      return;
    }
    async function load() {
      try {
        const details = await getInterview(interviewId);
        if (details.status === "COMPLETED") {
          router.replace(`/results/${interviewId}`);
          return;
        }
        const started = details.status === "CREATED" ? await startInterview(interviewId) : null;
        const loadedQuestions = await getInterviewQuestions(interviewId);
        setInterview(details);
        setQuestions(loadedQuestions);
        if (started?.currentQuestion) setCurrentIndex(Math.max(0, started.currentQuestion.questionOrder - 1));
        const stored = window.localStorage.getItem(draftKey(interviewId));
        if (stored) setAnswers(JSON.parse(stored) as Record<number, DraftAnswer>);
      } catch (requestError) {
        setError(requestError instanceof ApiError ? requestError.message : "Could not load this interview.");
      } finally {
        setIsLoading(false);
      }
    }
    load();
  }, [interviewId, router]);

  useEffect(() => {
    if (!isLoading) window.localStorage.setItem(draftKey(interviewId), JSON.stringify(answers));
  }, [answers, interviewId, isLoading]);

  useEffect(() => {
    if (isLoading) return;
    const timer = window.setInterval(() => setElapsedSeconds((value) => value + 1), 1000);
    return () => window.clearInterval(timer);
  }, [isLoading]);

  const question = questions[currentIndex];
  const currentAnswer = question ? answers[question.id] || {} : {};
  const answeredCount = useMemo(() => questions.filter((item) => {
    const value = answers[item.id];
    return Boolean(value?.text?.trim() || value?.option || value?.options?.length);
  }).length, [answers, questions]);
  const unansweredCount = questions.length - answeredCount;
  const progress = questions.length ? ((currentIndex + 1) / questions.length) * 100 : 0;

  function updateAnswer(value: DraftAnswer) {
    if (question) setAnswers((current) => ({ ...current, [question.id]: value }));
  }

  function formatTime(seconds: number) {
    return `${String(Math.floor(seconds / 60)).padStart(2, "0")}:${String(seconds % 60).padStart(2, "0")}`;
  }

  function toRequest(item: InterviewQuestion, value: DraftAnswer): AnswerRequest | null {
    if (item.questionType === "MULTIPLE_SELECT") return value.options?.length ? { selectedOptions: value.options } : null;
    if (["MCQ", "TRUE_FALSE"].includes(item.questionType)) return value.option ? { selectedOption: value.option } : null;
    return value.text?.trim() ? { answerText: value.text.trim() } : null;
  }

  async function submitInterview() {
    if (!window.confirm(`Submit this interview? ${unansweredCount} question${unansweredCount === 1 ? " remains" : "s remain"} unanswered.`)) return;
    setError("");
    setIsSubmitting(true);
    try {
      for (const item of questions) {
        const request = toRequest(item, answers[item.id] || {});
        if (request) await submitAnswer(interviewId, item.id, request);
      }
      await finishInterview(interviewId);
      window.localStorage.removeItem(draftKey(interviewId));
      router.push(`/results/${interviewId}`);
    } catch (requestError) {
      setError(requestError instanceof ApiError ? requestError.message : "Could not submit this interview.");
    } finally {
      setIsSubmitting(false);
    }
  }

  const isObjective = question && ["MCQ", "MULTIPLE_SELECT", "TRUE_FALSE"].includes(question.questionType);
  const typeLabel: Record<InterviewQuestionType, string> = {
    TEXT: "Open response", MCQ: "Multiple choice", MULTIPLE_SELECT: "Select all that apply",
    TRUE_FALSE: "True or false", TECHNICAL: "Technical", BEHAVIORAL: "Behavioral", HR: "HR", SCENARIO: "Scenario",
  };

  return <main className="page-shell"><nav className="topbar"><Link className="brand" href="/dashboard"><span className="brand-mark">●</span> Interview Lab</Link><span className="session-timer" aria-label="Elapsed interview time">{formatTime(elapsedSeconds)}</span><Link className="text-link" href="/history">Exit session</Link></nav>{isLoading ? <div className="page-state">Loading interview...</div> : error && !questions.length ? <section className="dashboard"><p className="form-error" role="alert">{error}</p><Link className="text-link" href="/history">Return to history</Link></section> : <section className="session-shell"><div className="session-heading"><div><span className="eyebrow">{interview?.jobRole}</span><h1>One thoughtful answer at a time.</h1><p>{interview?.interviewType} · {interview?.difficulty}</p></div><span className="eyebrow">{answeredCount} / {questions.length} answered</span></div>{error && <p className="form-error" role="alert">{error}</p>}<div className="session-progress"><div className="progress-label"><span>Question {currentIndex + 1} of {questions.length}</span><span>{unansweredCount} unanswered</span></div><div className="progress-track"><div className="progress-value" style={{ width: `${progress}%` }} /></div></div>{question && <div className="question-card"><span className="question-type">{typeLabel[question.questionType]}</span><h2>{question.questionText}</h2>{isObjective ? <div className="option-list" role={question.questionType === "MULTIPLE_SELECT" ? "group" : "radiogroup"} aria-label="Answer options">{question.options.map((option) => <label className="option-choice" key={option}><input type={question.questionType === "MULTIPLE_SELECT" ? "checkbox" : "radio"} name={`question-${question.id}`} checked={question.questionType === "MULTIPLE_SELECT" ? currentAnswer.options?.includes(option) : currentAnswer.option === option} onChange={() => question.questionType === "MULTIPLE_SELECT" ? updateAnswer({ options: currentAnswer.options?.includes(option) ? currentAnswer.options.filter((item) => item !== option) : [...(currentAnswer.options || []), option] }) : updateAnswer({ option })} /> <span>{option}</span></label>)}</div> : <><label htmlFor="answer">Your answer</label><textarea id="answer" value={currentAnswer.text || ""} onChange={(event) => updateAnswer({ text: event.target.value })} placeholder="Take a moment, then write your answer..." maxLength={10000} /><div className="character-count">{(currentAnswer.text || "").length} / 10000</div></>}<div className="session-actions"><button className="ghost-button" onClick={() => setCurrentIndex((index) => Math.max(0, index - 1))} disabled={currentIndex === 0 || isSubmitting}>Previous</button>{currentIndex === questions.length - 1 ? <button className="primary-button" onClick={submitInterview} disabled={isSubmitting}>{isSubmitting ? "Submitting interview..." : "Submit interview"}</button> : <button className="primary-button" onClick={() => setCurrentIndex((index) => Math.min(questions.length - 1, index + 1))} disabled={isSubmitting}>Next</button>}</div></div>}</section>}</main>;
}

export default function InterviewSessionPage() { return <ProtectedRoute><SessionContent /></ProtectedRoute>; }
