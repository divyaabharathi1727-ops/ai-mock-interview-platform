import Link from "next/link";
import { ArrowRight, CheckCircle2, Sparkles, TrendingUp } from "lucide-react";
import { Button } from "@/components/ui/button";
import { ScoreCard } from "@/components/score-card";

export default function ResultsPage() {
  return (
    <div className="min-h-screen bg-slate-50 px-4 py-10 sm:px-6 lg:px-8">
      <div className="mx-auto max-w-6xl rounded-[2rem] border border-slate-200 bg-white p-6 shadow-[0_20px_60px_rgba(15,23,42,0.06)] sm:p-8">
        <div className="mb-8 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <p className="text-sm uppercase tracking-[0.18em] text-slate-500">Interview results</p>
            <h1 className="mt-2 text-3xl font-bold text-slate-900">Frontend Developer • Technical</h1>
          </div>
          <Link href="/interview">
            <Button className="rounded-full bg-slate-900 px-5 py-2.5 text-white hover:bg-slate-800">
              Try Another Interview
            </Button>
          </Link>
        </div>

        <div className="grid gap-6 lg:grid-cols-[0.85fr_1.15fr]">
          <div className="rounded-[2rem] border border-slate-200 bg-slate-50 p-6">
            <div className="flex items-center gap-3">
              <div className="flex h-14 w-14 items-center justify-center rounded-2xl bg-slate-900 text-xl font-bold text-white">86</div>
              <div>
                <p className="text-sm text-slate-500">Overall score</p>
                <p className="text-4xl font-bold tracking-tight text-slate-900">86/100</p>
              </div>
            </div>

            <div className="mt-8 space-y-4">
              <ScoreCard label="Technical Accuracy" value={88} accent="bg-slate-900" />
              <ScoreCard label="Communication" value={90} accent="bg-emerald-500" />
              <ScoreCard label="Relevance" value={84} accent="bg-violet-500" />
              <ScoreCard label="Completeness" value={82} accent="bg-amber-500" />
            </div>
          </div>

          <div className="space-y-6">
            <div className="rounded-[2rem] border border-slate-200 bg-white p-6">
              <div className="mb-5 flex items-center gap-2 text-slate-900">
                <Sparkles className="h-5 w-5 text-violet-500" />
                <h2 className="text-xl font-bold">AI feedback</h2>
              </div>
              <p className="text-base leading-7 text-slate-600">
                You communicated clearly and explained your reasoning in a structured way. Your answer was technically strong and showed a good understanding of performance trade-offs. To improve further, focus on shortening your introductory statements and provide more concrete examples tied to real user impact.
              </p>
            </div>

            <div className="grid gap-6 md:grid-cols-2">
              <div className="rounded-[2rem] border border-slate-200 bg-white p-6">
                <div className="mb-4 flex items-center gap-2 text-emerald-600">
                  <CheckCircle2 className="h-5 w-5" />
                  <h3 className="text-lg font-semibold text-slate-900">Strengths</h3>
                </div>
                <ul className="space-y-3 text-sm text-slate-600">
                  <li>• Clear structure and reasoning</li>
                  <li>• Relevant technical examples</li>
                  <li>• Good trade-off awareness</li>
                </ul>
              </div>

              <div className="rounded-[2rem] border border-slate-200 bg-white p-6">
                <div className="mb-4 flex items-center gap-2 text-amber-600">
                  <TrendingUp className="h-5 w-5" />
                  <h3 className="text-lg font-semibold text-slate-900">Weaknesses</h3>
                </div>
                <ul className="space-y-3 text-sm text-slate-600">
                  <li>• Less detail on measurable impact</li>
                  <li>• More concise final summary needed</li>
                  <li>• Could add deeper roadmap examples</li>
                </ul>
              </div>
            </div>

            <div className="rounded-[2rem] border border-slate-200 bg-white p-6">
              <h3 className="text-xl font-bold text-slate-900">Recommended improvements</h3>
              <ul className="mt-4 space-y-3 text-sm text-slate-600">
                <li className="rounded-2xl border border-slate-200 bg-slate-50 p-3">Use a four-part answer structure: problem, approach, trade-offs, and metrics.</li>
                <li className="rounded-2xl border border-slate-200 bg-slate-50 p-3">Add specifics on user-facing impact and monitoring strategy.</li>
                <li className="rounded-2xl border border-slate-200 bg-slate-50 p-3">Keep the closing summary concise and outcome-focused.</li>
              </ul>
            </div>
          </div>
        </div>

        <div className="mt-8 flex justify-center">
          <Link href="/dashboard">
            <Button variant="outline" className="rounded-full border-slate-200 bg-white px-5 py-2.5 text-slate-700 hover:bg-slate-100">
              Back to dashboard
              <ArrowRight className="ml-2 h-4 w-4" />
            </Button>
          </Link>
        </div>
      </div>
    </div>
  );
}
