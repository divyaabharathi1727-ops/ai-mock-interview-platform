"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { ProtectedRoute } from "@/components/ProtectedRoute";
import { ApiError, deleteInterview, getInterviews } from "@/lib/api";
import type { Interview } from "@/types/auth";

function formatDate(value: string) { return new Intl.DateTimeFormat("en", { dateStyle: "medium" }).format(new Date(value)); }

function HistoryContent() {
	const [interviews, setInterviews] = useState<Interview[]>([]);
	const [isLoading, setIsLoading] = useState(true);
	const [deletingId, setDeletingId] = useState<number | null>(null);
	const [error, setError] = useState("");

	useEffect(() => {
		getInterviews().then(setInterviews).catch((requestError) => setError(requestError instanceof ApiError ? requestError.message : "Could not load interview history.")).finally(() => setIsLoading(false));
	}, []);

	async function handleDelete(id: number) {
		setDeletingId(id);
		setError("");
		try { await deleteInterview(id); setInterviews((current) => current.filter((interview) => interview.id !== id)); }
		catch (requestError) { setError(requestError instanceof ApiError ? requestError.message : "Could not delete this interview."); }
		finally { setDeletingId(null); }
	}

	return <main className="page-shell"><nav className="topbar"><Link className="brand" href="/dashboard"><span className="brand-mark">●</span> Interview Lab</Link><Link className="text-link" href="/interview">New interview</Link></nav><section className="dashboard"><div className="dashboard-header"><div><span className="eyebrow">Practice history</span><h1>Your progress, in view.</h1></div><Link className="primary-button compact-button" href="/interview">Start interview</Link></div>{error && <p className="form-error" role="alert">{error}</p>}{isLoading ? <p className="muted-copy">Loading interviews...</p> : interviews.length === 0 ? <div className="dashboard-panel empty-state"><h2>No interviews yet.</h2><p>Start your first mock interview and practice your skills.</p><Link className="text-link" href="/interview">Start your first interview</Link></div> : <div className="interview-list">{interviews.map((interview) => <article className="interview-row" key={interview.id}><div><h2>{interview.jobRole}</h2><p>{interview.interviewType} · {interview.difficulty} · {formatDate(interview.createdAt)}</p></div><div className="row-actions"><span className={`status status-${interview.status.toLowerCase()}`}>{interview.status.replace("_", " ")}</span><button className="ghost-button" onClick={() => handleDelete(interview.id)} disabled={deletingId === interview.id}>{deletingId === interview.id ? "Deleting..." : "Delete"}</button></div></article>)}</div>}</section></main>;
}

export default function HistoryPage() { return <ProtectedRoute><HistoryContent /></ProtectedRoute>; }
