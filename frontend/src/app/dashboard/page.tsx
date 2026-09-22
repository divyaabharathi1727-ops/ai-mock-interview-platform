"use client";

import { ProtectedRoute } from "@/components/ProtectedRoute";
import { useAuth } from "@/components/AuthProvider";

function DashboardContent() {
  const { user, logout } = useAuth();
  return <main className="page-shell"><nav className="topbar"><span className="brand"><span className="brand-mark">●</span> Interview Lab</span><button className="ghost-button" onClick={logout}>Sign out</button></nav><section className="dashboard"><div className="dashboard-header"><div><span className="eyebrow">Your workspace</span><h1>Hello, {user?.name.split(" ")[0]}.</h1></div><span className="eyebrow">{user?.email}</span></div><div className="dashboard-grid"><article className="dashboard-panel"><h2>Start an interview</h2><p>Choose a role and practice a focused technical or behavioral round.</p></article><article className="dashboard-panel"><h2>Your progress</h2><p>Review the answers you have practiced and spot patterns over time.</p></article><article className="dashboard-panel"><h2>Profile</h2><p>{user?.role} account. More personalization is coming soon.</p></article></div></section></main>;
}

export default function DashboardPage() { return <ProtectedRoute><DashboardContent /></ProtectedRoute>; }
