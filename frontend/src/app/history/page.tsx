"use client";
import { ProtectedRoute } from "@/components/ProtectedRoute";
export default function HistoryPage() { return <ProtectedRoute><main className="page-shell"><section className="dashboard"><span className="eyebrow">Practice history</span><h1>Your progress, in view.</h1><div className="dashboard-panel"><h2>No sessions yet</h2><p>Complete your first interview to see feedback here.</p></div></section></main></ProtectedRoute>; }
