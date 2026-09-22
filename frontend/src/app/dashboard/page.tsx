"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { ProtectedRoute } from "@/components/ProtectedRoute";
import { useAuth } from "@/components/AuthProvider";
import { ApiError, getInterviews } from "@/lib/api";
import type { Interview } from "@/types/auth";

function DashboardContent() {
  const { user, logout } = useAuth();
  const [interviews, setInterviews] = useState<Interview[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState("");
  useEffect(() => { getInterviews().then(setInterviews).catch((requestError) => setError(requestError instanceof ApiError ? requestError.message : "Could not load interview data.")).finally(() => setIsLoading(false)); }, []);
  const completed = interviews.filter((interview) => interview.status === "COMPLETED").length;
  const inProgress = interviews.filter((interview) => interview.status === "IN_PROGRESS").length;
  return <main className="page-shell"><nav className="topbar"><Link className="brand" href="/dashboard"><span className="brand-mark">●</span> Interview Lab</Link><button className="ghost-button" onClick={logout}>Sign out</button></nav><section className="dashboard"><div className="dashboard-header"><div><span className="eyebrow">Your workspace</span><h1>Hello, {user?.name.split(" ")[0]}.</h1></div><span className="eyebrow">{user?.email}</span></div>{error && <p className="form-error" role="alert">{error}</p>}<div className="stats-grid"><article><span>Total interviews</span><strong>{isLoading ? "—" : interviews.length}</strong></article><article><span>Completed</span><strong>{isLoading ? "—" : completed}</strong></article><article><span>In progress</span><strong>{isLoading ? "—" : inProgress}</strong></article></div><div className="dashboard-grid"><article className="dashboard-panel"><h2>Start an interview</h2><p>Choose a role and practice a focused technical or behavioral round.</p><Link className="text-link" href="/interview">Set up a round</Link></article><article className="dashboard-panel"><h2>Your progress</h2><p>{isLoading ? "Loading your practice data..." : interviews.length ? "Review your recent practice and keep building momentum." : "Your first practice round is waiting."}</p><Link className="text-link" href="/history">View history</Link></article><article className="dashboard-panel"><h2>Profile</h2><p>{user?.role} account. Signed in as {user?.email}.</p></article></div></section></main>;
}

export default function DashboardPage() { return <ProtectedRoute><DashboardContent /></ProtectedRoute>; }
