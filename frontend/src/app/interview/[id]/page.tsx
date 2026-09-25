"use client";

import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { ProtectedRoute } from "@/components/ProtectedRoute";
import { ApiError, finishInterview, getCurrentQuestion, getInterview, startInterview, submitAnswer } from "@/lib/api";
import type { Interview, InterviewQuestion, InterviewSession } from "@/types/auth";

function SessionContent() {
  const params = useParams<{ id: string }>();
  const router = useRouter();
  const interviewId = Number(params.id);
  const [interview, setInterview] = useState<Interview | null>(null);
  const [session, setSession] = useState<InterviewSession | null>(null);
  const [answer, setAnswer] = useState("");
  const [selectedOption, setSelectedOption] = useState("");
  const [selectedOptions, setSelectedOptions] = useState<string[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isFinishing, setIsFinishing] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!Number.isInteger(interviewId)) {
      setError("That interview could not be found.");
      setIsLoading(false);
      return;
    }
    async function loadSession() {
      try {
        const details = await getInterview(interviewId);
        setInterview(details);
        if (details.status === "COMPLETED") {
          router.replace(`/results/${interviewId}`);
          return;
        }
        const started = details.status === "CREATED"
          ? await startInterview(interviewId)
          : await getCurrentQuestion(interviewId);
        setSession(started);
      } catch (requestError) {
        setError(requestError instanceof ApiError ? requestError.message : "Could not load this interview.");
      } finally {
        setIsLoading(false);
      }
    }
    loadSession();
  }, [interviewId, router]);

  async function handleAnswer() {
    const question = session?.currentQuestion;
    if (!question) return;
    const isMultiple = question.questionType === "MULTIPLE_SELECT";
    const isObjective = isMultiple || question.questionType === "MCQ" || question.questionType === "TRUE_FALSE";
    if ((isMultiple && selectedOptions.length === 0) || (!isMultiple && isObjective && !selectedOption) || (!isObjective && !answer.trim())) {
      setError(isObjective ? "Select an answer before submitting." : "Write an answer before submitting.");
      return;
    }
    setError("");
    setIsSubmitting(true);
    try {
      const response = await submitAnswer(interviewId, question.id, isMultiple
        ? { selectedOptions }
        : isObjective ? { selectedOption } : { answerText: answer.trim() });
      setSession({
        currentQuestion: response.nextQuestion,
        readyToFinish: response.readyToFinish,
        answeredQuestions: response.answeredQuestions,
        totalQuestions: response.totalQuestions,
      });
      setAnswer("");
      setSelectedOption("");
      setSelectedOptions([]);
    } catch (requestError) {
      setError(requestError instanceof ApiError ? requestError.message : "Could not save your answer.");
    } finally {
      setIsSubmitting(false);
    }
  }

  async function handleFinish() {
    setError("");
    setIsFinishing(true);
    try {
      await finishInterview(interviewId);
      router.push(`/results/${interviewId}`);
    } catch (requestError) {
      setError(requestError instanceof ApiError ? requestError.message : "Could not finish this interview.");
    } finally {
      setIsFinishing(false);
    }
  }

  const question = session?.currentQuestion as InterviewQuestion | null;
  const progress = session && session.totalQuestions ? (session.answeredQuestions / session.totalQuestions) * 100 : 0;

  const isObjective = question && ["MCQ", "MULTIPLE_SELECT", "TRUE_FALSE"].includes(question.questionType);
  return <main className="page-shell"><nav className="topbar"><Link className="brand" href="/dashboard"><span className="brand-mark">●</span> Interview Lab</Link><Link className="text-link" href="/history">Exit session</Link></nav>{isLoading ? <div className="page-state">Starting interview...</div> : error && !session ? <section className="dashboard"><p className="form-error" role="alert">{error}</p><Link className="text-link" href="/history">Return to history</Link></section> : <section className="session-shell"><div className="session-heading"><div><span className="eyebrow">Mock interview</span><h1>{interview?.jobRole}</h1><p>{interview?.interviewType} interview · {interview?.difficulty}</p></div><span className="eyebrow">Mixed question session</span></div>{error && <p className="form-error" role="alert">{error}</p>}<div className="session-progress"><div className="progress-label"><span>Question {session && question ? question.questionOrder : session?.totalQuestions} / {session?.totalQuestions}</span><span>{session?.answeredQuestions} answered</span></div><div className="progress-track"><div className="progress-value" style={{ width: `${progress}%` }} /></div></div>{question ? <div className="question-card"><span className="question-type">{question.questionType === "TEXT" ? "Open response" : question.questionType.replace("_", " ")}</span><h2>{question.questionText}</h2>{isObjective ? <div className="option-list">{question.options.map((option) => <label className="option-choice" key={option}><input type={question.questionType === "MULTIPLE_SELECT" ? "checkbox" : "radio"} name="objective-answer" checked={question.questionType === "MULTIPLE_SELECT" ? selectedOptions.includes(option) : selectedOption === option} onChange={() => question.questionType === "MULTIPLE_SELECT" ? setSelectedOptions((current) => current.includes(option) ? current.filter((value) => value !== option) : [...current, option]) : setSelectedOption(option)} disabled={isSubmitting} /> <span>{option}</span></label>)}</div> : <><label htmlFor="answer">Your answer</label><textarea id="answer" value={answer} onChange={(event) => setAnswer(event.target.value)} placeholder="Take a moment, then write your answer..." maxLength={10000} disabled={isSubmitting} /></>}<div className="answer-footer"><span>{isObjective ? "Select the best answer" : `${answer.length} / 10000`}</span><button className="primary-button" onClick={handleAnswer} disabled={isSubmitting}>{isSubmitting ? isObjective ? "Checking answer..." : "Luna is evaluating..." : "Submit answer"}</button></div></div> : <div className="question-card"><span className="question-type">Ready</span><h2>You have answered every question.</h2><p className="muted-copy">Review your responses, then finish this session when you are ready.</p></div>}{session?.readyToFinish && <button className="finish-button" onClick={handleFinish} disabled={isFinishing}>{isFinishing ? "Finishing interview..." : "Finish interview"}</button>}</section>}</main>;
}

export default function InterviewSessionPage() { return <ProtectedRoute><SessionContent /></ProtectedRoute>; }
