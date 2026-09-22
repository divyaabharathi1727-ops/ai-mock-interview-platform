"use client";

import { createContext, useContext, useEffect, useState, type ReactNode } from "react";
import { clearAuth, getStoredUser, getToken, storeAuth } from "@/lib/auth";
import { getCurrentUser, login as loginRequest, register as registerRequest } from "@/lib/api";
import type { AuthResponse, User } from "@/types/auth";

interface AuthContextValue {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (email: string, password: string) => Promise<AuthResponse>;
  register: (name: string, email: string, password: string) => Promise<AuthResponse>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const token = getToken();
    const storedUser = getStoredUser();
    if (!token) {
      setIsLoading(false);
      return;
    }

    getCurrentUser()
      .then((currentUser) => {
        storeAuth(token, currentUser);
        setUser(currentUser);
      })
      .catch(() => {
        clearAuth();
        setUser(null);
      })
      .finally(() => setIsLoading(false));

    if (storedUser) setUser(storedUser);
  }, []);

  async function login(email: string, password: string) {
    const response = await loginRequest(email, password);
    storeAuth(response.token, response.user);
    setUser(response.user);
    return response;
  }

  async function register(name: string, email: string, password: string) {
    return registerRequest(name, email, password);
  }

  function logout() {
    clearAuth();
    setUser(null);
    window.location.assign("/login");
  }

  return (
    <AuthContext.Provider value={{ user, isAuthenticated: Boolean(user), isLoading, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used within an AuthProvider");
  return context;
}
