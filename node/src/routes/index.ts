import { Router } from 'express'

import categoria from './categorias'
import usuarios from './usuarios'
import session from './session'
import autenticacao from '../middlewares/autenticacao'

const routes = Router()

routes.use('/usuarios', usuarios)
routes.use('/session', session)
routes.use(autenticacao)
routes.use('/categorias', autenticacao, categoria)

export default routes
