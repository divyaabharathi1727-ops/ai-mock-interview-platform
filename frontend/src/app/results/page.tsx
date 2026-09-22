"use client";
import { ProtectedRoute } from "@/components/ProtectedRoute";
export default function ResultsPage() { return <ProtectedRoute><main className="page-shell"><section className="dashboard"><span className="eyebrow">Feedback</span><h1>Make every answer count.</h1><div className="dashboard-panel"><h2>Results will appear here</h2><p>Interview feedback will be available after your next practice round.</p></div></section></main></ProtectedRoute>; }
