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

  const updateBodySchema = z.object({
    nome: z.string().optional(),
    email: z.string().email().optional(),
    senha: z.string().min(6).optional()
  })

  try {
    const updateData = updateBodySchema.parse(req.body)

    if (updateData.senha) {
      updateData.senha = await hash(updateData.senha, 8)
    }
    
    const updatedCount = await knex('usuarios')
      .where({ id })
      .update(updateData)
    
    if (updatedCount === 0) {
      res.status(404).json({ message: 'Usuário não encontrado' })
    }
    
    const usuarioAtualizado = await knex('usuarios')
      .where({ id })
      .first()

    res.json({
      message: 'Usuário atualizado com sucesso',
      usuario: usuarioAtualizado
    })      

  } catch (error) {
    // Tratamento adequado do erro
    if (error instanceof z.ZodError) {
      res.status(400).json({
        message: 'Dados inválidos',
        errors: error.errors
      })
    }
    
    console.error('Erro ao atualizar usuário:', error)
    res.status(500).json({ message: 'Erro interno ao atualizar usuário' })
  }
})


router.delete('/:id', async (req, res) => {
  const deleteParamsSchema = z.object({
    id: z.string().min(1)
  })

  const id = +deleteParamsSchema.parse(req.params).id

  try {

    const usuario = await knex('usuarios').where({ id }).first()

    if (!usuario) {
      res.status(404).json({ message: 'Usuário não encontrado' })
    }

    // Se existir, deleta
    await knex('usuarios').where({ id }).delete()

    res.status(200).json({
      message: 'Usuário deletado com sucesso',
      usuarioDeletado: usuario
    })

  } catch (error) {
    // Erro de validação do Zod (ID inválido)
    if (error instanceof z.ZodError) {
      res.status(400).json({
        message: 'ID inválido',
        errors: error.errors
      })
    }

    // Outros erros (ex: banco de dados)
    console.error('Erro ao deletar usuário:', error)
    res.status(500).json({
      message: 'Erro interno ao deletar usuário'
    })
  }
})

export default router
