import express from 'express';
import knex from '../db/knex';
import { z } from 'zod';
import { hash } from 'bcrypt';

const router = express.Router();

/////////////////////////////////////BUSCAR////////////////////////////////////////////
router.get('/', (req, res) => {
  knex('usuarios').then((cursos) => {
    res.json({ cursos: cursos })
  })
});

/////////////////////////////////////BUSCAR ID////////////////////////////////////////////
router.get("/:id", async (req, res) => {
  try {
    const { id } = req.params;

    const usuario = await knex('usuarios').where({ id: id })

      .select('id', 'foto_url', 'nome', 'email', 'curso_id')
      .first();

    if (!usuario) { res.status(404).json({ message: "Usuário não encontrado." }) }
    res.status(200).json(usuario);

  } catch (error) {
    console.error(error);
    res.status(500).json({ message: "Erro interno no servidor." });
  }
});
////////////////////////////////////CRIAR/////////////////////////////////////////////
router.post("/", async (req, res) => {
  const registerBodySchema = z
    .object({
      foto_url: z.string(),
      nome: z.string(),
      email: z.string().email(),
      senha_hash: z.string().min(6, { message: "Senha tem que ter no mínimo 6 dígitos" }),
      confirmarSenha_hash: z.string().min(6, { message: "Senha tem que ter no mínimo 6 dígitos" }),
      curso_id: z.coerce.number(),
    }).refine((data) => data.senha_hash === data.confirmarSenha_hash, {
      message: "As senhas não são iguais",
      path: ["confirmarSenha_hash"],
    });

  try {
    const dados = registerBodySchema.parse(req.body);

    const existeUsuario = await knex("usuarios")
      .where({ email: dados.email })
      .first();

    if (existeUsuario) { res.status(400).json({ errors: [{ message: "E-mail já cadastrado." }] }) }

    const senhaHash = await hash(dados.senha_hash, 10);

    await knex("usuarios").insert({
      foto_url: dados.foto_url,
      nome: dados.nome,
      email: dados.email,
      senha_hash: senhaHash,
      curso_id: dados.curso_id,
    });

     res.status(201).json({ message: "Usuário cadastrado com sucesso!" });

  } catch (error) {
    if (error instanceof z.ZodError) {
       res.status(400).json({
        errors: error.errors.map(err => ({ message: err.message }))
      });
    }

    console.error("Erro no cadastro:", error);
     res.status(500).json({ message: "Erro interno do servidor." });
  }
});

////////////////////////////////////ATUALIZAR//////////////////////////////////////////
router.put('/:id', async (req, res) => {
  const idParams = z.object({ id: z.string().min(1) });
  const id = +idParams.parse(req.params).id;

  const updateBodySchema = z.object({
    foto_url: z.string().optional(),
    nome: z.string().optional(),
    senha_hash: z.string().min(6, { message: "Senha tem que ter no minimo 6 digitos" }).optional()
  });

  try {
    const updateBody = updateBodySchema.parse(req.body);

    const atualizar: { foto_url?: string; nome?: string; senha_hash?: string } = {};

    if (updateBody.foto_url) atualizar.foto_url = updateBody.foto_url;
    if (updateBody.nome) atualizar.nome = updateBody.nome;
    if (updateBody.senha_hash) {
      atualizar.senha_hash = await hash(updateBody.senha_hash, 10);
    }

    if (Object.keys(atualizar).length === 0) {
      res.status(400).json({ message: 'Nenhum dado enviado para atualizar' });
    }

    const updateAccount = await knex('usuarios')
      .where({ id })
      .update(atualizar);

    if (updateAccount === 0) {
      res.status(404).json({ message: 'Usuário não encontrado' });
    }

    const usuarioAtualizado = await knex('usuarios')
      .where({ id })
      .first()

    res.json({
      message: 'Usuário atualizado com sucesso',
      usuario: usuarioAtualizado
    })
  } catch (error) {
    if (error instanceof z.ZodError) {
      res.status(400).json({
        message: 'Dados inválidos',
        errors: error.errors
      })
    }

    console.error('Erro ao atualizar usuário:', error)
    res.status(500).json({ message: 'Erro interno ao atualizar usuário' })
  }
});

////////////////////////////////////DELETA//////////////////////////////////////////
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

    await knex('usuarios').where({ id }).delete()

    res.status(200).json({
      message: 'Usuário deletado com sucesso',
      usuarioDeletado: usuario
    })

  } catch (error) {
    if (error instanceof z.ZodError) {
      res.status(400).json({
        message: 'ID inválido',
        errors: error.errors
      })
    }
    console.error('Erro ao deletar usuário:', error)
    res.status(500).json({
      message: 'Erro interno ao deletar usuário'
    })
  }
})

export default router;