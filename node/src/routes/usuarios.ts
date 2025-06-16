import express from 'express';
import knex from '../db/knex';
import { z } from 'zod';
import { hash } from 'bcrypt';

const router = express.Router();

router.get("/:id", async (req, res) => {
  try {
    const { id } = req.params;

    const usuario = await knex('usuarios')
      .where({ id: id })

      .select('id', 'foto_url', 'nome', 'email',  'curso_id')
      .first();

    if (!usuario) {
      res.status(404).json({ message: "Usuário não encontrado." });
    }
    res.status(200).json(usuario);

  } catch (error) {

    console.error(error);
    res.status(500).json({ message: "Erro interno no servidor." });
  }
});

router.post("/", async (req, res) => {

  const registerBodySchema = z.object({
    foto_url: z.string(),
    nome: z.string(),
    email: z.string().email(),
    senha_hash: z.string().min(6, { message: "Senha tem que ter no minimo 6 digitos" }),
    curso_id: z.coerce.number()
  });

  const dados = registerBodySchema.parse(req.body);

  const existeUsuario = await knex('usuarios')
    .where({ email: dados.email })
    .first()

  if (existeUsuario) {
    res.status(400).json({ errors: [{ message: 'E-mail já cadastrado ' }] });
  }

  const senhaHash = await hash(dados.senha_hash, 10);

  await knex('usuarios')
    .insert({
      foto_url: dados.foto_url,
      nome: dados.nome,
      email: dados.email,
      senha_hash: senhaHash,
      curso_id: dados.curso_id
    });

  res.status(201).json({
    message: 'Usuário cadastrado com sucesso!',
  });
});

export default router;