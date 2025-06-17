import { Router } from 'express';
import knex from '../db/knex';

const router = Router();

router.get('/', (req, res) => {
  knex('eventos')

    .join('cursos', 'eventos.curso_id', 'cursos.id')
    .join('palestrantes', 'eventos.palestrante_id', 'palestrantes.id')

    .select(
      'eventos.*',
      'cursos.nome as nome_curso',
      'palestrantes.nome as nome_palestrante'
    )
    .then((eventosComNomes) => {
      res.json({ eventos: eventosComNomes })
    })
    .catch((err) => {
      console.error(err)
      res.status(500).json({ error: 'Ocorreu um erro ao buscar os eventos.' })
    })
});

router.get('/:id', (req, res) => {
  knex('cursos').then((cursos) => {
    res.json({ cursos: cursos })
  })
});

export default router;