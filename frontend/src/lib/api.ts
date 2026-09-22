import { clearAuth, getToken } from "@/lib/auth";
import type { ApiErrorResponse, AuthResponse, User } from "@/types/auth";

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
