import { Router } from 'express';
import knex from '../db/knex';

const router = Router();

router.get('/', (req, res) => {
  knex('cursos').then((cursos) => {
    res.json({ cursos: cursos })
  })
});

export default router;