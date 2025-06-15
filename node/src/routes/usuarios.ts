import { Router } from 'express'
import knex from '../db/knex'
import { z } from 'zod'
import { hash } from 'bcrypt'

const router = Router()

router.get('/', (req, res) => {
  knex('usuarios').then((users) => {
    res.json({ usuarios: users })
  })
})

router.post("/", async (req, res) => {

  const registerBodySchema = z.object({
    nome: z.string(),
    email: z.string().email(),
    senha: z.string().min(6)
  })

  const objSalvar = registerBodySchema.parse(
    req.body
  )

  objSalvar.senha = await hash(objSalvar.senha, 8)

  const id_usuario = await knex('usuarios').insert(objSalvar)

  const usuarios = await knex('usuarios')
    .where({
      id: id_usuario[0]
    })

  res.json({
    message: 'Usuario cadastrado com sucesso',
    usuario: usuarios
  })

})

router.put('/:id', async (req, res) => {

  const registerParamsSchema = z.object({
    id: z.string().min(1)
  })

  const id = +registerParamsSchema.parse(req.params).id

  //segue o trabalho

})


export default router
