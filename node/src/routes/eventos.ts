import { Router } from 'express'
import knex from '../db/knex'
const router = Router()

router.get('/', (req, res) => {
  knex('eventos').then((event) => {
    res.json({ eventos: event })
  })
})

export default router