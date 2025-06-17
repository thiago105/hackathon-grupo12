import Router from 'express'
import knex from '../db/knex';
import { z } from 'zod';

const router = Router();

/////////////////////////////////////BUSCAR DADOS////////////////////////////////////////////
router.get('/', (req, res) => {
  knex('inscricoes')
    .join('usuarios', 'inscricoes.usuario_id', 'usuarios.id')
    .join('eventos', 'inscricoes.evento_id', 'eventos.id')

    .select(
      'inscricoes.*',
      'usuarios.nome as nome_usuario',
      'eventos.nome as nome_eventos'
    )

    .then((eventosComNomes) => {
      res.json({ inscricoes: eventosComNomes })
    })
    .catch((err) => {
      console.error(err)
      res.status(500).json({ error: 'Ocorreu um erro ao buscar os eventos.' })
    })
});

/////////////////////////////////////CRIAR////////////////////////////////////////////
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
