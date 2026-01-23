const amqp = require("amqplib");

const QUEUE_NAME = "test_queue";

async function connectWithRetry() {
  while (true) {
    try {
      const connection = await amqp.connect("amqp://user:password@rabbitmq:5672");

      console.log("Consumer connected to RabbitMQ");

      const channel = await connection.createChannel();
      await channel.assertQueue(QUEUE_NAME);

      channel.consume(QUEUE_NAME, msg => {
        console.log("Received:", msg.content.toString());
        channel.ack(msg);
      });

      break;
    } catch (err) {
      console.log("RabbitMQ not ready, retrying in 5s...");
      await new Promise(res => setTimeout(res, 5000));
    }
  }
}

connectWithRetry();
