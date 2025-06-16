// const express = require('express')
import express, { Request, Response, NextFunction } from 'express'
import cors from 'cors'
import routes from './routes'

import { ZodError } from 'zod'

const app = express()
app.use(cors())

app.use(express.json())

const PORT = 3306

app.use(routes)

app.use((
  error: Error,
  req: Request,
  res: Response,
  next: NextFunction
) => {
  if (res.headersSent) {
    return next(error);
  }

  if (error instanceof ZodError) {
    res.status(400)
      .send({
        message: "Erro de validação",
        issues: error.format()
      })
      return
  }

  console.log(error)
  res.status(500).json({
    status: "erro",
    message: "Lascou"
  })
  return
})

app.listen(PORT, () => {
  console.log('Express iniciou na porta:' +
    PORT
  )
})