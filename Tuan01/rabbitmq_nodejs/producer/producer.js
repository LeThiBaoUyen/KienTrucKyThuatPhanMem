const express = require("express");
const amqp = require("amqplib");

const app = express();
app.use(express.json());

const QUEUE_NAME = "test_queue";

async function connectWithRetry() {
  while (true) {
    try {
      const connection = await amqp.connect("amqp://user:password@rabbitmq:5672");

      console.log("Connected to RabbitMQ");
      return connection;
    } catch (err) {
      console.log("RabbitMQ not ready, retrying in 5s...");
      await new Promise(res => setTimeout(res, 5000));
    }
  }
}

async function start() {
  const connection = await connectWithRetry();
  const channel = await connection.createChannel();
  await channel.assertQueue(QUEUE_NAME);

  console.log("Queue ready:", QUEUE_NAME);

  app.post("/send", (req, res) => {
    const { message } = req.body;

    if (!message) {
      return res.status(400).json({ error: "message is required" });
    }

    channel.sendToQueue(
      QUEUE_NAME,
      Buffer.from(message)
    );

    res.json({
      success: true,
      sent: message
    });
  });

  app.listen(3000, "0.0.0.0", () => {
    console.log("Producer running on port 3000");
  });
}

start();
