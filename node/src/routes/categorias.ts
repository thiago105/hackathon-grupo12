import { Router } from 'express'
import knex from '../db/knex'

const routes = Router()

routes.get('/', (requisicao, resposta) => {
  knex('categorias').then((banco) => {
    resposta.json({ categorias: banco })
  })
})

routes.post('/', (req, res) => {

  if (!req.body?.nome) {

    res.status(404).json({
      mensagem: "Nome é obrigatorio!"
    })
    return
  }

  knex('categorias').insert(req.body)
    .then(() => {
      res.json({ mensagem: 'Cadastrou uma nova categoria =D' })
    })
})

export default routes
