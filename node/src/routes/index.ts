import { Router } from 'express'

import session from './session'
import eventos from './eventos'
import login from './login'
import autenticacao from '../middlewares/autenticacao'

const routes = Router()

routes.use('/login', login)
routes.use('/eventos', eventos)
routes.use('/session', session)
routes.use(autenticacao)

export default routes
