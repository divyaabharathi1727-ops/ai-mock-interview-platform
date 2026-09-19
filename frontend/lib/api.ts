import axios from "axios";

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_BASE_URL ?? "/api",
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});

export const apiEndpoints = {
  register: "/auth/register",
  login: "/auth/login",
  interviews: "/interviews",
  dashboardStats: "/dashboard/stats",
};

export default api;
