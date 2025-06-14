import { Router, Request, Response } from 'express';
import { z } from 'zod';
import knex from '../database';

const palestrantesRouter = Router();

// Schema de validação para palestrantes
const palestranteSchema = z.object({
  nome: z.string().min(1, 'Nome é obrigatório'),
  mini_curriculo: z.string().min(1, 'Mini currículo é obrigatório'),
  tema: z.string().min(1, 'Tema é obrigatório'),
  foto_url: z.string().optional()
});

// Listar todos os palestrantes
palestrantesRouter.get('/', async (req: Request, res: Response) => {
  try {
    const palestrantes = await knex('palestrantes').select('*');
    res.json(palestrantes);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Erro ao buscar palestrantes' });
  }
});

// Buscar palestrante por ID
palestrantesRouter.get('/:id', async (req: Request, res: Response) => {
  try {
    const { id } = req.params;
    const palestrante = await knex('palestrantes')
      .where({ id })
      .first();

    if (!palestrante) {
      res.status(404).json({ error: 'Palestrante não encontrado' });
      return;
    }

    res.json(palestrante);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Erro ao buscar palestrante' });
  }
});

// Criar novo palestrante
palestrantesRouter.post('/', async (req: Request, res: Response) => {
  try {
    const palestranteData = palestranteSchema.parse(req.body);
    
    const [palestrante] = await knex('palestrantes')
      .insert(palestranteData)
      .returning('*');

    res.status(201).json(palestrante);
  } catch (error) {
    if (error instanceof z.ZodError) {
      res.status(400).json({ error: error.errors });
      return;
    }
    console.error(error);
    res.status(500).json({ error: 'Erro ao criar palestrante' });
  }
});

// Atualizar palestrante
palestrantesRouter.put('/:id', async (req: Request, res: Response) => {
  try {
    const { id } = req.params;
    const palestranteData = palestranteSchema.parse(req.body);

    const [palestrante] = await knex('palestrantes')
      .where({ id })
      .update(palestranteData)
      .returning('*');

    if (!palestrante) {
      res.status(404).json({ error: 'Palestrante não encontrado' });
      return;
    }

    res.json(palestrante);
  } catch (error) {
    if (error instanceof z.ZodError) {
      res.status(400).json({ error: error.errors });
      return;
    }
    console.error(error);
    res.status(500).json({ error: 'Erro ao atualizar palestrante' });
  }
});

// Deletar palestrante
palestrantesRouter.delete('/:id', async (req: Request, res: Response) => {
  try {
    const { id } = req.params;
    const deleted = await knex('palestrantes')
      .where({ id })
      .delete();

    if (!deleted) {
      res.status(404).json({ error: 'Palestrante não encontrado' });
      return;
    }

    res.status(204).send();
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Erro ao deletar palestrante' });
  }
});

export default palestrantesRouter; 