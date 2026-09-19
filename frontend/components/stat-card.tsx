import { ArrowUpRight } from "lucide-react";

interface StatCardProps {
  label: string;
  value: string;
  change?: string;
  tone?: "slate" | "emerald" | "violet" | "amber";
}

const toneMap = {
  slate: "bg-slate-900 text-white",
  emerald: "bg-emerald-500 text-white",
  violet: "bg-violet-500 text-white",
  amber: "bg-amber-500 text-white",
};

export function StatCard({ label, value, change, tone = "slate" }: StatCardProps) {
  return (
    <div className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
      <div className="mb-5 flex items-center justify-between">
        <span className="text-sm font-medium text-slate-500">{label}</span>
        <div className={`rounded-full px-2 py-1 text-[10px] font-semibold ${toneMap[tone]}`}>
          {change ?? "+12%"}
        </div>
      </div>
      <div className="flex items-end justify-between">
        <div className="text-3xl font-bold tracking-tight text-slate-900">{value}</div>
        <ArrowUpRight className="h-4 w-4 text-slate-400" />
      </div>
    </div>
  );
}
