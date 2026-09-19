import Link from "next/link";
import { ArrowUpRight, CalendarDays, CircleDashed, Clock3 } from "lucide-react";
import { type Interview } from "@/types";

interface InterviewCardProps {
  interview: Interview;
}

export function InterviewCard({ interview }: InterviewCardProps) {
  return (
    <div className="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm transition hover:border-slate-300">
      <div className="mb-4 flex items-start justify-between gap-4">
        <div>
          <p className="text-xs font-semibold uppercase tracking-[0.18em] text-slate-500">{interview.type}</p>
          <h3 className="mt-2 text-lg font-semibold text-slate-900">{interview.jobRole}</h3>
        </div>
        <div className="rounded-full bg-emerald-50 px-2.5 py-1 text-xs font-semibold text-emerald-700">
          {interview.score}%
        </div>
      </div>

      <div className="mb-4 grid gap-2 text-sm text-slate-500 sm:grid-cols-3">
        <div className="flex items-center gap-2">
          <CalendarDays className="h-4 w-4" />
          {interview.date}
        </div>
        <div className="flex items-center gap-2">
          <Clock3 className="h-4 w-4" />
          {interview.difficulty}
        </div>
        <div className="flex items-center gap-2">
          <CircleDashed className="h-4 w-4" />
          {interview.status}
        </div>
      </div>

      <Link
        href="/results"
        className="inline-flex items-center gap-2 text-sm font-semibold text-slate-900 transition hover:text-slate-700"
      >
        View result
        <ArrowUpRight className="h-4 w-4" />
      </Link>
    </div>
  );
}
