import { Router } from 'express'

import eventosRouter from './eventos'
import palestrantesRouter from './palestrantes'
import inscricoesRouter from './inscricoes'
import usuariosRouter from './usuarios'
import sessionRouter from './session'
import certificadosRouter from './certificados'
import autenticacao from '../middlewares/autenticacao'

const routes = Router()

// Rotas públicas
routes.use('/session', sessionRouter)
routes.use('/usuarios', usuariosRouter)

// Rotas protegidas
routes.use(autenticacao)
routes.use('/eventos', eventosRouter)
routes.use('/palestrantes', palestrantesRouter)
routes.use('/inscricoes', inscricoesRouter)
routes.use('/certificados', certificadosRouter)

export default routes
