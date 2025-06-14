import { Router, Request, Response } from 'express';
import { z } from 'zod';
import knex from '../database';

const inscricoesRouter = Router();

// Schema de validação para inscrições
const inscricaoSchema = z.object({
  usuario_id: z.number().int().positive(),
  evento_id: z.number().int().positive(),
  aprovado: z.boolean().default(false)
});

// Listar todas as inscrições
inscricoesRouter.get('/', async (req: Request, res: Response) => {
  try {
    const inscricoes = await knex('inscricoes')
      .select(
        'inscricoes.*',
        'usuarios.nome as usuario_nome',
        'eventos.nome as evento_nome'
      )
      .leftJoin('usuarios', 'inscricoes.usuario_id', 'usuarios.id')
      .leftJoin('eventos', 'inscricoes.evento_id', 'eventos.id');
    
    res.json(inscricoes);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Erro ao buscar inscrições' });
  }
});

// Buscar inscrição por ID
inscricoesRouter.get('/:id', async (req: Request, res: Response) => {
  try {
    const { id } = req.params;
    const inscricao = await knex('inscricoes')
      .where({ id })
      .select(
        'inscricoes.*',
        'usuarios.nome as usuario_nome',
        'eventos.nome as evento_nome'
      )
      .leftJoin('usuarios', 'inscricoes.usuario_id', 'usuarios.id')
      .leftJoin('eventos', 'inscricoes.evento_id', 'eventos.id')
      .first();

    if (!inscricao) {
      res.status(404).json({ error: 'Inscrição não encontrada' });
      return;
    }

    res.json(inscricao);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Erro ao buscar inscrição' });
  }
});

// Criar nova inscrição
inscricoesRouter.post('/', async (req: Request, res: Response) => {
  try {
    const inscricaoData = inscricaoSchema.parse(req.body);
    
    // Verificar se já existe inscrição
    const inscricaoExistente = await knex('inscricoes')
      .where({
        usuario_id: inscricaoData.usuario_id,
        evento_id: inscricaoData.evento_id
      })
      .first();

    if (inscricaoExistente) {
      res.status(400).json({ error: 'Usuário já inscrito neste evento' });
      return;
    }

    const [inscricao] = await knex('inscricoes')
      .insert(inscricaoData)
      .returning('*');

    res.status(201).json(inscricao);
  } catch (error) {
    if (error instanceof z.ZodError) {
      res.status(400).json({ error: error.errors });
      return;
    }
    console.error(error);
    res.status(500).json({ error: 'Erro ao criar inscrição' });
  }
});

// Atualizar status da inscrição
inscricoesRouter.patch('/:id/aprovar', async (req: Request, res: Response) => {
  try {
    const { id } = req.params;
    const [inscricao] = await knex('inscricoes')
      .where({ id })
      .update({ aprovado: true })
      .returning('*');

    if (!inscricao) {
      res.status(404).json({ error: 'Inscrição não encontrada' });
      return;
    }

    res.json(inscricao);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Erro ao aprovar inscrição' });
  }
});

// Deletar inscrição
inscricoesRouter.delete('/:id', async (req: Request, res: Response) => {
  try {
    const { id } = req.params;
    const deleted = await knex('inscricoes')
      .where({ id })
      .delete();

    if (!deleted) {
      res.status(404).json({ error: 'Inscrição não encontrada' });
      return;
    }

    res.status(204).send();
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Erro ao deletar inscrição' });
  }
});

export default inscricoesRouter; 