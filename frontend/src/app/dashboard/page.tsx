"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { ProtectedRoute } from "@/components/ProtectedRoute";
import { useAuth } from "@/components/AuthProvider";
import { ApiError, getInterviewAnalytics, getInterviews } from "@/lib/api";
import type { Interview, InterviewAnalytics } from "@/types/auth";

function DashboardContent() {
  const { user, logout } = useAuth();
  const [interviews, setInterviews] = useState<Interview[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState("");
  const [analytics, setAnalytics] = useState<InterviewAnalytics | null>(null);
  useEffect(() => { Promise.all([getInterviews(), getInterviewAnalytics()]).then(([loaded, summary]) => { setInterviews(loaded); setAnalytics(summary); }).catch((requestError) => setError(requestError instanceof ApiError ? requestError.message : "Could not load interview data.")).finally(() => setIsLoading(false)); }, []);
  const completed = interviews.filter((interview) => interview.status === "COMPLETED").length;
  const inProgress = interviews.filter((interview) => interview.status === "IN_PROGRESS").length;
  return <main className="page-shell"><nav className="topbar"><Link className="brand" href="/dashboard"><span className="brand-mark">●</span> Interview Lab</Link><button className="ghost-button" onClick={logout}>Sign out</button></nav><section className="dashboard"><div className="dashboard-header"><div><span className="eyebrow">Your workspace</span><h1>Hello, {user?.name.split(" ")[0]}.</h1></div><span className="eyebrow">{user?.email}</span></div>{error && <p className="form-error" role="alert">{error}</p>}<div className="stats-grid"><article><span>Total interviews</span><strong>{isLoading ? "—" : analytics?.totalInterviews ?? interviews.length}</strong></article><article><span>Average score</span><strong>{analytics?.averageScore == null ? "—" : `${analytics.averageScore.toFixed(1)}/10`}</strong></article><article><span>Best score</span><strong>{analytics?.bestScore == null ? "—" : `${analytics.bestScore.toFixed(1)}/10`}</strong></article></div><div className="analytics-strip"><div><span>Average duration</span><strong>{analytics?.averageDurationSeconds == null ? "—" : `${Math.floor(analytics.averageDurationSeconds / 60)}m`}</strong></div><div><span>Answered</span><strong>{analytics?.totalAnsweredQuestions ?? "—"}</strong></div><div><span>Objective correct</span><strong>{analytics?.totalCorrectObjectiveAnswers ?? "—"}</strong></div></div>{analytics?.trend.length ? <div className="trend-panel"><div className="panel-heading"><span className="eyebrow">Performance trend</span><span>Completed interviews</span></div>{analytics.trend.map((point, index) => <div className="trend-row" key={`${point.label}-${index}`}><span>{point.label}</span><div className="trend-bar"><i style={{ width: `${(point.score || 0) * 10}%` }} /></div><strong>{point.score == null ? "—" : point.score.toFixed(1)}</strong></div>)}</div> : <div className="dashboard-panel empty-state"><h2>{isLoading ? "Loading analytics..." : "No completed interviews yet."}</h2><p>Complete an interview to see your personal performance trend.</p></div>}<div className="dashboard-grid"><article className="dashboard-panel"><h2>Start an interview</h2><p>Choose a role and practice a focused technical or behavioral round.</p><Link className="text-link" href="/interview">Set up a round</Link></article><article className="dashboard-panel"><h2>Your progress</h2><p>{interviews.length ? "Review your recent practice and keep building momentum." : "Your first practice round is waiting."}</p><Link className="text-link" href="/history">View history</Link></article><article className="dashboard-panel"><h2>Profile</h2><p>{user?.role} account. Signed in as {user?.email}.</p></article></div></section></main>;
}

export default function DashboardPage() { return <ProtectedRoute><DashboardContent /></ProtectedRoute>; }
