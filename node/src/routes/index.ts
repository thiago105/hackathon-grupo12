import { Router } from 'express';

import cursos from './cursos';
import usuarios from './usuarios';
import eventos from './eventos';
import login from './login';
import palestrantes from './palestrantes';
import inscricoes from './inscricoes';
import certificado from './certificado';

import autenticacao from '../middlewares/autenticacao';

const routes = Router();

routes.use('/cursos', cursos);
routes.use('/usuarios', usuarios);
routes.use('/eventos', eventos);
routes.use('/login', login);
routes.use('/palestrantes', palestrantes);
routes.use('/inscricoes', inscricoes);
routes.use('/certificado', certificado);

routes.use(autenticacao);

export default routes;