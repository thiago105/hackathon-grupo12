import Router from 'express'
import knex from '../db/knex';
import { z } from 'zod';
import { hash } from 'bcrypt';

const router = Router();

router.post("/", async (req, res) => {
  const registerBodySchema = z.object({
    usuario_id: z.number(),
    evento_id: z.number()
  });

  if (!registerBodySchema) {
    res.status(400).json({ message: 'usuario_id ou evento_id são obrigatórios' });
    return
  }

  const body = registerBodySchema.parse(req.body);
  try {

    const inscricaoExistente = await knex('inscricoes')
      .where({ usuario_id: body.usuario_id, evento_id: body.evento_id })
      .first()

    if (inscricaoExistente) { res.status(400).json({ message: 'Você já se inscreveu nesse evento' }) }

    await knex('inscricoes')
      .insert({
        usuario_id: body.usuario_id,
        evento_id: body.evento_id
      });

    res.status(201).json({
      message: 'Inscrição realizada com sucesso!',
    });
  } catch (error) {
    res.status(400).json({ message: 'Dados inválidos ou incompletos.' });
  }
});

export default router;
