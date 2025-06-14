// const express = require('express')
import express, { Request, Response, NextFunction, ErrorRequestHandler } from 'express'
import cors from 'cors'
import { config } from 'dotenv'
import routes from './routes'

import { ZodError } from 'zod'

// Carregar variáveis de ambiente
config()

const app = express()
app.use(cors())

app.use(express.json())

app.use(routes)

// Middleware de erro (deve ser o último)
const errorHandler: ErrorRequestHandler = (err, req, res, next) => {
  if (res.headersSent) {
    next(err);
    return;
  }

  if (err instanceof ZodError) {
    res.status(400).json({
      message: "Erro de validação",
      issues: err.format()
    });
    return;
  }

  console.error(err);
  res.status(500).json({
    message: "Erro interno do servidor"
  });
};

app.use(errorHandler);

// Iniciar servidor
const PORT = process.env.PORT || 3001
app.listen(PORT, () => {
  console.log(`Servidor rodando na porta ${PORT}`);
})
