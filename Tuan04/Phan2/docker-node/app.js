const http = require("http");

const server = http.createServer((req, res) => {
  res.end("Hello, Docker!");
});

server.listen(3000, () => {
  console.log("Server running on port 3000");
});
// docker build -t docker-mode .
// docker run -p 3000:3000 docker-node