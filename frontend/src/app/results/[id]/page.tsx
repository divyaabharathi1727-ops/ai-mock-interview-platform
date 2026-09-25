"use client";

import Link from "next/link";
import { useParams } from "next/navigation";
import { useEffect, useState } from "react";
import { ProtectedRoute } from "@/components/ProtectedRoute";
import { ApiError, getInterviewResults } from "@/lib/api";
import type { InterviewResults } from "@/types/auth";

function formatDuration(seconds: number | null) {
  if (seconds === null) return "Unavailable";
  return `${Math.floor(seconds / 60)}m ${seconds % 60}s`;
}

function ResultsContent() {
  const params = useParams<{ id: string }>();
  const [results, setResults] = useState<InterviewResults | null>(null);
  const [error, setError] = useState("");
  useEffect(() => {
    getInterviewResults(Number(params.id)).then(setResults).catch((requestError) => setError(requestError instanceof ApiError ? requestError.message : "Could not load results."));
  }, [params.id]);

  return <main className="page-shell"><nav className="topbar"><Link className="brand" href="/dashboard"><span className="brand-mark">●</span> Interview Lab</Link><Link className="text-link" href="/history">History</Link></nav><section className="results-shell"><span className="eyebrow">Session complete</span><h1>Interview completed.</h1>{error && <p className="form-error" role="alert">{error}</p>}{!results && !error ? <p className="muted-copy">Loading results...</p> : results && <><p className="results-intro">Your answers were evaluated by the backend. AI evaluation is shown only when it was available.</p><div className="results-grid"><div><span>Job role</span><strong>{results.jobRole}</strong></div><div><span>Questions</span><strong>{results.totalQuestions}</strong></div><div><span>Answered</span><strong>{results.answeredQuestions}</strong></div><div><span>Unanswered</span><strong>{results.unansweredQuestions}</strong></div><div><span>Correct objective</span><strong>{results.correctObjectiveAnswers}</strong></div><div><span>Incorrect objective</span><strong>{results.incorrectObjectiveAnswers}</strong></div><div><span>Duration</span><strong>{formatDuration(results.durationSeconds)}</strong></div><div><span>Overall score</span><strong>{results.overallScore === null ? "Unavailable" : `${results.overallScore.toFixed(1)} / 10`}</strong></div><div><span>Technical</span><strong>{results.technicalScore === null ? "Unavailable" : `${results.technicalScore.toFixed(1)} / 10`}</strong></div><div><span>Clarity</span><strong>{results.clarityScore === null ? "Unavailable" : `${results.clarityScore.toFixed(1)} / 10`}</strong></div></div><div className="result-review">{results.questions.map((question) => <article className={`review-card review-${question.status.toLowerCase()}`} key={question.questionId}><span className="question-type">{question.questionType.replace("_", " ")} · {question.status.replace("_", " ")}</span><h2>{question.questionText}</h2><p><strong>Your answer:</strong> {question.candidateAnswer || "Not answered"}</p>{question.evaluation && <div className="evaluation-copy"><p><strong>{question.evaluation.available ? question.evaluation.feedback || question.status : "AI evaluation unavailable"}</strong>{question.evaluation.overallScore !== null ? ` · ${question.evaluation.overallScore.toFixed(1)} / 10` : ""}</p></div>}</article>)}</div><div className="results-actions"><Link className="primary-button compact-button" href="/interview">Start another interview</Link><Link className="text-link" href="/history">Back to history</Link></div></>}</section></main>;
}

export default function ResultsPage() { return <ProtectedRoute><ResultsContent /></ProtectedRoute>; }
