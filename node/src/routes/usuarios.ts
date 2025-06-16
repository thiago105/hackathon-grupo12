import Router from 'express'
import knex from '../db/knex';
import { z } from 'zod';
import { hash } from 'bcrypt';

const router = Router();

router.post("/", async (req, res) => {
  const registerBodySchema = z.object({
    foto_url: z.string(),
    nome: z.string(),
    email: z.string().email(),
    senha_hash: z.string(),
    curso_id: z.number()
  });

  const dados = registerBodySchema.parse(req.body);

  const existeUsuario = await knex('usuarios')
    .where({ email: dados.email })
    .first()

  if (existeUsuario) { res.status(400).json({ message: 'E-mail já cadastrado.' }) }

  const senhaHash = await hash(dados.senha_hash, 10);

  await knex('inscricoes')
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