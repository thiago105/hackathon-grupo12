import { Router } from 'express';

import cursos from './cursos';
import eventos from './eventos';
import login from './login';
import autenticacao from '../middlewares/autenticacao';

const routes = Router();

routes.use('/login', login);
routes.use('/cursos', cursos);
routes.use('/eventos', eventos);
routes.use(autenticacao);

export default routes;
