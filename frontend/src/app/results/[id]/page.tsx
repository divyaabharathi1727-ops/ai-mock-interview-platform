"use client";

import Link from "next/link";
import { useParams } from "next/navigation";
import { useEffect, useState } from "react";
import { ProtectedRoute } from "@/components/ProtectedRoute";
import { ApiError, getInterviewResults } from "@/lib/api";
import type { InterviewResults } from "@/types/auth";

function ResultsContent() {
  const params = useParams<{ id: string }>();
  const [results, setResults] = useState<InterviewResults | null>(null);
  const [error, setError] = useState("");
  useEffect(() => {
    const id = Number(params.id);
    getInterviewResults(id).then(setResults).catch((requestError) => setError(requestError instanceof ApiError ? requestError.message : "Could not load results."));
  }, [params.id]);
  return <main className="page-shell"><nav className="topbar"><Link className="brand" href="/dashboard"><span className="brand-mark">●</span> Interview Lab</Link><Link className="text-link" href="/history">History</Link></nav><section className="results-shell"><span className="eyebrow">Session complete</span><h1>Interview completed.</h1>{error && <p className="form-error" role="alert">{error}</p>}{!results && !error ? <p className="muted-copy">Loading results...</p> : results && <><p className="results-intro">Here is the factual record of this practice session. AI evaluation will be added in a later sprint.</p><div className="results-grid"><div><span>Job role</span><strong>{results.jobRole}</strong></div><div><span>Interview type</span><strong>{results.interviewType}</strong></div><div><span>Difficulty</span><strong>{results.difficulty}</strong></div><div><span>Questions</span><strong>{results.totalQuestions}</strong></div><div><span>Answers</span><strong>{results.answeredQuestions}</strong></div><div><span>Completed</span><strong>{results.completedAt ? new Intl.DateTimeFormat("en", { dateStyle: "medium", timeStyle: "short" }).format(new Date(results.completedAt)) : "Not finished"}</strong></div></div><div className="results-actions"><Link className="primary-button compact-button" href="/interview">Start another interview</Link><Link className="text-link" href="/history">Back to history</Link></div></>}</section></main>;
}

export default function ResultsPage() { return <ProtectedRoute><ResultsContent /></ProtectedRoute>; }
