import { Router } from 'express'

import usuarios from './usuarios'
import session from './session'
import eventos from './eventos'
import autenticacao from '../middlewares/autenticacao'

const routes = Router()

routes.use('/usuarios', usuarios)
routes.use('/eventos', eventos)
routes.use('/session', session)
routes.use(autenticacao)

export default routes
