import Link from "next/link";
import { Bell, ChevronRight, Plus, TrendingUp } from "lucide-react";
import { Sidebar } from "@/components/sidebar";
import { StatCard } from "@/components/stat-card";
import { PerformanceChart } from "@/components/performance-chart";
import { InterviewCard } from "@/components/interview-card";
import { type Interview } from "@/types";

const interviews: Interview[] = [
  { id: "1", jobRole: "Frontend Developer", type: "Technical", difficulty: "Medium", status: "Completed", score: 89, date: "Jun 28" },
  { id: "2", jobRole: "Full Stack Developer", type: "Mixed", difficulty: "Hard", status: "Completed", score: 76, date: "Jun 17" },
  { id: "3", jobRole: "Data Analyst", type: "Technical", difficulty: "Medium", status: "In Progress", score: 82, date: "Jun 09" },
];

export default function DashboardPage() {
  return (
    <div className="min-h-screen bg-slate-50">
      <div className="flex min-h-screen">
        <Sidebar />

        <main className="flex-1">
          <header className="border-b border-slate-200 bg-white/80 backdrop-blur-xl">
            <div className="flex items-center justify-between px-4 py-4 sm:px-6 lg:px-8">
              <div>
                <p className="text-sm text-slate-500">Welcome back</p>
                <h1 className="text-2xl font-bold tracking-tight text-slate-900">Ava Johnson</h1>
              </div>

              <div className="flex items-center gap-3">
                <button
                  type="button"
                  className="relative flex h-10 w-10 items-center justify-center rounded-full border border-slate-200 bg-white text-slate-600"
                  aria-label="Notifications"
                >
                  <Bell className="h-4 w-4" />
                  <span className="absolute right-2 top-2 h-2 w-2 rounded-full bg-emerald-500" />
                </button>
                <div className="flex items-center gap-3 rounded-full border border-slate-200 bg-white px-2 py-1.5 shadow-sm">
                  <div className="flex h-9 w-9 items-center justify-center rounded-full bg-slate-900 text-sm font-semibold text-white">AJ</div>
                  <div className="hidden text-left md:block">
                    <p className="text-sm font-semibold text-slate-900">Ava</p>
                    <p className="text-xs text-slate-500">Student</p>
                  </div>
                </div>
              </div>
            </div>
          </header>

          <div className="space-y-6 p-4 sm:p-6 lg:p-8">
            <section className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
              <StatCard label="Total Interviews" value="18" change="+3 this month" tone="slate" />
              <StatCard label="Average Score" value="84%" change="+6%" tone="emerald" />
              <StatCard label="Best Score" value="94%" change="+12%" tone="violet" />
              <StatCard label="Latest Score" value="89%" change="+4%" tone="amber" />
            </section>

            <section className="rounded-[2rem] border border-slate-200 bg-white p-5 shadow-sm sm:p-6">
              <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
                <div>
                  <p className="text-sm font-medium text-slate-500">Ready to practice?</p>
                  <h2 className="mt-1 text-2xl font-bold text-slate-900">Start a new mock interview</h2>
                </div>
                <Link href="/interview">
                  <button
                    type="button"
                    className="inline-flex items-center justify-center gap-2 rounded-full bg-slate-900 px-5 py-3 text-sm font-medium text-white transition hover:bg-slate-800"
                  >
                    <Plus className="h-4 w-4" />
                    Start New Interview
                  </button>
                </Link>
              </div>
            </section>

            <section className="grid gap-6 xl:grid-cols-[1.2fr_0.8fr]">
              <div className="rounded-[2rem] border border-slate-200 bg-white p-5 shadow-sm sm:p-6">
                <div className="mb-6 flex items-center justify-between">
                  <div>
                    <p className="text-sm text-slate-500">Interview history</p>
                    <h2 className="text-2xl font-bold text-slate-900">Recent Interviews</h2>
                  </div>
                  <Link href="/history" className="inline-flex items-center gap-1 text-sm font-semibold text-slate-700 hover:text-slate-900">
                    View all <ChevronRight className="h-4 w-4" />
                  </Link>
                </div>

                <div className="space-y-4">
                  {interviews.map((interview) => (
                    <InterviewCard key={interview.id} interview={interview} />
                  ))}
                </div>
              </div>

              <div className="rounded-[2rem] border border-slate-200 bg-white p-5 shadow-sm sm:p-6">
                <div className="mb-5 flex items-center justify-between">
                  <div>
                    <p className="text-sm text-slate-500">Insights</p>
                    <h2 className="text-2xl font-bold text-slate-900">Recommended improvements</h2>
                  </div>
                  <TrendingUp className="h-5 w-5 text-emerald-500" />
                </div>

                <ul className="space-y-4 text-sm text-slate-600">
                  {[
                    "Sharpen your explanation of trade-offs in technical decisions.",
                    "Use clearer examples when answering behavioral questions.",
                    "Improve the structure of your final summary and call-to-action.",
                  ].map((item) => (
                    <li key={item} className="flex gap-3 rounded-2xl border border-slate-200 bg-slate-50 p-3">
                      <span className="mt-1 h-2.5 w-2.5 rounded-full bg-slate-900" />
                      <span>{item}</span>
                    </li>
                  ))}
                </ul>
              </div>
            </section>

            <section className="rounded-[2rem] border border-slate-200 bg-white p-5 shadow-sm sm:p-6">
              <div className="mb-5 flex items-center justify-between">
                <div>
                  <p className="text-sm text-slate-500">Performance overview</p>
                  <h2 className="text-2xl font-bold text-slate-900">Score trend</h2>
                </div>
                <div className="rounded-full bg-emerald-50 px-3 py-1 text-sm font-semibold text-emerald-700">+12% since last month</div>
              </div>
              <PerformanceChart />
            </section>
          </div>
        </main>
      </div>
    </div>
  );
}
