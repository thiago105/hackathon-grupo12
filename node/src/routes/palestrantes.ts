import { Router } from 'express';
import knex from '../db/knex';
const router = Router();

/////////////////////////////////////BUSCAR ID////////////////////////////////////////////
router.get('/:id', (req, res) => {
  knex('palestrantes').then((palestrante) => {
    res.json({ palestrantes: palestrante })
  })
});

export default router;