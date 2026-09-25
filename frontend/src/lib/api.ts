import { clearAuth, getToken } from "@/lib/auth";
import type {
  ApiErrorResponse,
  AuthResponse,
  AnswerRequest,
  AnswerResponse,
  Interview,
  InterviewCompletion,
  InterviewCreateRequest,
  InterviewQuestion,
  InterviewResults,
  InterviewSession,
  InterviewUpdateRequest,
  User,
} from "@/types/auth";

const API_URL = (process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080").replace(/\/$/, "");

export class ApiError extends Error {
  status: number;

  constructor(message: string, status: number) {
    super(message);
    this.name = "ApiError";
    this.status = status;
  }
}

async function apiRequest<T>(path: string, options: RequestInit = {}): Promise<T> {
  const headers = new Headers(options.headers);
  headers.set("Content-Type", "application/json");

  const token = getToken();
  if (token) headers.set("Authorization", `Bearer ${token}`);

  let response: Response;
  try {
    response = await fetch(`${API_URL}${path}`, { ...options, headers });
  } catch {
    throw new ApiError("The backend is unavailable. Please try again.", 0);
  }

  const body = await response.json().catch(() => null) as ApiErrorResponse | T | null;
  if (!response.ok) {
    if (response.status === 401) clearAuth();
    const message = body && typeof body === "object" && "message" in body && body.message
      ? body.message
      : response.status === 401
        ? "Your session has expired. Please sign in again."
        : "Something went wrong. Please try again.";
    throw new ApiError(message, response.status);
  }

  return body as T;
}

export function login(email: string, password: string): Promise<AuthResponse> {
  return apiRequest<AuthResponse>("/api/auth/login", {
    method: "POST",
    body: JSON.stringify({ email, password }),
  });
}

export function register(name: string, email: string, password: string): Promise<AuthResponse> {
  return apiRequest<AuthResponse>("/api/auth/register", {
    method: "POST",
    body: JSON.stringify({ name, email, password }),
  });
}

export function getCurrentUser(): Promise<User> {
  return apiRequest<User>("/api/users/me");
}

export function createInterview(request: InterviewCreateRequest): Promise<Interview> {
  return apiRequest<Interview>("/api/interviews", {
    method: "POST",
    body: JSON.stringify(request),
  });
}

export function getInterviews(): Promise<Interview[]> {
  return apiRequest<Interview[]>("/api/interviews");
}

export function getInterview(id: number): Promise<Interview> {
  return apiRequest<Interview>(`/api/interviews/${id}`);
}

export function updateInterview(id: number, request: InterviewUpdateRequest): Promise<Interview> {
  return apiRequest<Interview>(`/api/interviews/${id}`, {
    method: "PUT",
    body: JSON.stringify(request),
  });
}

export function deleteInterview(id: number): Promise<void> {
  return apiRequest<void>(`/api/interviews/${id}`, { method: "DELETE" });
}

export function startInterview(id: number): Promise<InterviewSession> {
  return apiRequest<InterviewSession>(`/api/interviews/${id}/start`, { method: "POST" });
}

export function getInterviewQuestions(id: number): Promise<InterviewQuestion[]> {
  return apiRequest<InterviewQuestion[]>(`/api/interviews/${id}/questions`);
}

export function getCurrentQuestion(id: number): Promise<InterviewSession> {
  return apiRequest<InterviewSession>(`/api/interviews/${id}/questions/current`);
}

export function submitAnswer(id: number, questionId: number, request: AnswerRequest): Promise<AnswerResponse> {
  return apiRequest<AnswerResponse>(`/api/interviews/${id}/questions/${questionId}/answer`, {
    method: "POST",
    body: JSON.stringify(request),
  });
}

export function finishInterview(id: number): Promise<InterviewCompletion> {
  return apiRequest<InterviewCompletion>(`/api/interviews/${id}/finish`, { method: "POST" });
}

export function getInterviewResults(id: number): Promise<InterviewResults> {
  return apiRequest<InterviewResults>(`/api/interviews/${id}/results`);
}

export function getEvaluation(interviewId: number, answerId: number) {
  return apiRequest<import("@/types/auth").AnswerEvaluation>(`/api/interviews/${interviewId}/answers/${answerId}/evaluation`);
}
