"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState, type FormEvent } from "react";
import { ProtectedRoute } from "@/components/ProtectedRoute";
import { ApiError, createInterview } from "@/lib/api";
import type { InterviewDifficulty } from "@/types/auth";

const jobRoles = ["Java Developer", "Full Stack Developer", "Frontend Developer", "Backend Developer", "Software Engineer"];
const interviewTypes = ["Technical", "HR", "Behavioral", "Mixed"];

function InterviewSetup() {
	const router = useRouter();
	const [jobRole, setJobRole] = useState(jobRoles[1]);
	const [interviewType, setInterviewType] = useState(interviewTypes[0]);
	const [difficulty, setDifficulty] = useState<InterviewDifficulty>("MEDIUM");
	const [error, setError] = useState("");
	const [isCreating, setIsCreating] = useState(false);

	async function handleSubmit(event: FormEvent<HTMLFormElement>) {
		event.preventDefault();
		setError("");
		setIsCreating(true);
		try {
			await createInterview({ jobRole, interviewType, difficulty });
			router.push("/history");
		} catch (requestError) {
			setError(requestError instanceof ApiError ? requestError.message : "Could not create the interview.");
		} finally {
			setIsCreating(false);
		}
	}

	return <main className="page-shell"><nav className="topbar"><Link className="brand" href="/dashboard"><span className="brand-mark">●</span> Interview Lab</Link><Link className="text-link" href="/history">History</Link></nav><section className="dashboard"><span className="eyebrow">Interview room</span><h1>Choose your practice round.</h1><form className="setup-form" onSubmit={handleSubmit}><label>Job role<select value={jobRole} onChange={(event) => setJobRole(event.target.value)}>{jobRoles.map((role) => <option key={role}>{role}</option>)}</select></label><label>Interview type<select value={interviewType} onChange={(event) => setInterviewType(event.target.value)}>{interviewTypes.map((type) => <option key={type}>{type}</option>)}</select></label><fieldset><legend>Difficulty</legend><div className="choice-row">{(["EASY", "MEDIUM", "HARD"] as InterviewDifficulty[]).map((level) => <label className="choice" key={level}><input type="radio" name="difficulty" value={level} checked={difficulty === level} onChange={() => setDifficulty(level)} /> {level}</label>)}</div></fieldset>{error && <p className="form-error" role="alert">{error}</p>}<button className="primary-button" type="submit" disabled={isCreating}>{isCreating ? "Creating interview..." : "Start interview"}</button><p className="muted-copy">Your setup is saved now. The question engine will be available in the next stage.</p></form></section></main>;
}

export default function InterviewPage() { return <ProtectedRoute><InterviewSetup /></ProtectedRoute>; }
