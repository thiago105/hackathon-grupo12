import Router from 'express'
import knex from '../db/knex';
import { z } from 'zod';

const router = Router();

/////////////////////////////////////BUSCAR ID////////////////////////////////////////////
router.get("/usuario/:usuario_id", async (req, res) => {
  const { usuario_id } = req.params;

  try {
    const inscricoes = await knex('inscricoes')
      .select('id', 'evento_id', 'aprovado')
      .where({ usuario_id });

    res.json({ inscricoes });
  } catch (error) {
    res.status(500).json({ errors: [{ message: 'Erro ao buscar inscrições' }] });
  }
});

/////////////////////////////////////BUSCAR DADOS////////////////////////////////////////////
router.get('/:id', (req, res) => {
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
  console.log("Body recebido:", req.body)
  const registerBodySchema = z.object({
    usuario_id: z.coerce.number(),
    evento_id: z.coerce.number()
  });

  try {
    const body = registerBodySchema.parse(req.body);

    const inscricaoExistente = await knex('inscricoes')
      .where({ usuario_id: body.usuario_id, evento_id: body.evento_id })
      .first();

    if (inscricaoExistente) {
      res.status(400).json({ errors: [{ message: 'Você já se inscreveu nesse evento' }] });
    }

    await knex('inscricoes').insert({
      usuario_id: body.usuario_id,
      evento_id: body.evento_id,
      aprovado: 0
    });


    res.status(201).json({ message: 'Inscrição realizada com sucesso!' });

  } catch (error) {
    console.error(error);
    res.status(400).json({ errors: [{ message: 'Dados inválidos ou incompletos.' }] });
  }
});
////////////////////////////////////DELETA//////////////////////////////////////////
router.delete('/:id', async (req, res) => {
  const deleteParamsSchema = z.object({
    id: z.string().min(1)
  })

  const id = +deleteParamsSchema.parse(req.params).id

  try {
    const usuario = await knex('inscricoes').where({ id }).first()

    if (!usuario) {
      res.status(404).json({ message: 'Inscrição não encontrada' })
    }

    await knex('inscricoes').where({ id }).delete()

    res.status(200).json({
      message: 'Inscrição deletada com sucesso',
      usuarioDeletado: usuario
    })

  } catch (error) {
    if (error instanceof z.ZodError) {
      res.status(400).json({
        message: 'ID inválido',
        errors: error.errors
      })
    }
    console.error('Erro ao deletar inscrição:', error)
    res.status(500).json({
      message: 'Erro interno ao deletar inscrição'
    })
  }
})

export default router;