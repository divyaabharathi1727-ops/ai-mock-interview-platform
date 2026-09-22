"use client";
import { ProtectedRoute } from "@/components/ProtectedRoute";
export default function InterviewPage() { return <ProtectedRoute><main className="page-shell"><section className="dashboard"><span className="eyebrow">Interview room</span><h1>Choose your practice round.</h1><div className="dashboard-panel"><h2>Coming next</h2><p>Your interview setup will live here.</p></div></section></main></ProtectedRoute>; }
