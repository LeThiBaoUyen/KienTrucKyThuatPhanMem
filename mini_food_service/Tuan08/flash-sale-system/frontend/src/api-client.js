import axios from 'axios';

const createApiClient = (baseURL) => {
  const client = axios.create({
    baseURL,
    headers: {
      'Content-Type': 'application/json'
    }
  });

  client.interceptors.response.use(
    response => response.data,
    error => {
      console.error('API Error:', error);
      throw error;
    }
  );

  return client;
};

export default createApiClient;
