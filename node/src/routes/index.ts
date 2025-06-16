import { Router } from 'express';

import cursos from './cursos';
import eventos from './eventos';
import login from './login';
import palestrantes from './palestrantes';
import autenticacao from '../middlewares/autenticacao';

const routes = Router();

routes.use('/login', login);
routes.use('/cursos', cursos);
routes.use('/eventos', eventos);
routes.use('/palestrantes', palestrantes);
routes.use(autenticacao);

export default routes;