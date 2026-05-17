import axios from "axios";

const API = axios.create({
  baseURL: "http://172.16.35.48:8080" // IP orchestrator
});

export const getTours = () => API.get("/tours");
export const bookTour = (data) => API.post("/book-tour", data);