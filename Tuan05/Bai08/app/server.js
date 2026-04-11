const express = require("express");
const mysql = require("mysql2/promise");

const app = express();
const PORT = 3000;

async function getDbVersion() {
  const connection = await mysql.createConnection({
    host: process.env.DB_HOST || "mysql",
    user: process.env.DB_USER || "user",
    password: process.env.DB_PASSWORD || "password",
    database: process.env.DB_NAME || "mydb"
  });

  const [rows] = await connection.query("SELECT VERSION() AS version");
  await connection.end();
  return rows[0].version;
}

app.get("/", async (req, res) => {
  try {
    const version = await getDbVersion();
    res.send(`Node.js connected to MySQL. Server version: ${version}`);
  } catch (error) {
    res.status(500).send(`Connection failed: ${error.message}`);
  }
});

app.listen(PORT, () => {
  console.log(`Bai 8 app is running on port ${PORT}`);
});
