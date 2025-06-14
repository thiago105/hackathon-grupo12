import { Router, Request, Response } from 'express';
import { z } from 'zod';
import knex from '../database';

const eventosRouter = Router();

// Schema de validação para eventos
const eventoSchema = z.object({
  nome: z.string().min(1, 'Nome é obrigatório'),
  data_inicio: z.string().transform(str => new Date(str)),
  data_fim: z.string().transform(str => new Date(str)),
  hora: z.string(),
  endereco: z.string().min(1, 'Endereço é obrigatório'),
  curso_id: z.number().int().positive(),
  palestrante_id: z.number().int().positive(),
  foto_url: z.string().optional()
});

// Listar todos os eventos
eventosRouter.get('/', async (req: Request, res: Response) => {
  try {
    const eventos = await knex('eventos')
      .select('eventos.*', 'cursos.nome as curso_nome', 'palestrantes.nome as palestrante_nome')
      .leftJoin('cursos', 'eventos.curso_id', 'cursos.id')
      .leftJoin('palestrantes', 'eventos.palestrante_id', 'palestrantes.id');
    
    res.json(eventos);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Erro ao buscar eventos' });
  }
});

// Buscar evento por ID
eventosRouter.get('/:id', async (req: Request, res: Response) => {
  try {
    const { id } = req.params;
    const evento = await knex('eventos')
      .where({ id })
      .select('eventos.*', 'cursos.nome as curso_nome', 'palestrantes.nome as palestrante_nome')
      .leftJoin('cursos', 'eventos.curso_id', 'cursos.id')
      .leftJoin('palestrantes', 'eventos.palestrante_id', 'palestrantes.id')
      .first();

    if (!evento) {
      res.status(404).json({ error: 'Evento não encontrado' });
      return;
    }

    res.json(evento);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Erro ao buscar evento' });
  }
});

// Criar novo evento
eventosRouter.post('/', async (req: Request, res: Response) => {
  try {
    const eventoData = eventoSchema.parse(req.body);

    // Verificar conflito de horário para o mesmo curso
    const conflito = await knex('eventos')
      .where('curso_id', eventoData.curso_id)
      .andWhere(function () {
        this.whereBetween('data_inicio', [eventoData.data_inicio, eventoData.data_fim])
          .orWhereBetween('data_fim', [eventoData.data_inicio, eventoData.data_fim])
          .orWhereRaw('? BETWEEN data_inicio AND data_fim', [eventoData.data_inicio])
          .orWhereRaw('? BETWEEN data_inicio AND data_fim', [eventoData.data_fim]);
      })
      .first();

    if (conflito) {
      res.status(400).json({ error: 'Já existe um evento para este curso nesse período.' });
      return;
    }

    const [evento] = await knex('eventos')
      .insert(eventoData)
      .returning('*');

    res.status(201).json(evento);
  } catch (error) {
    if (error instanceof z.ZodError) {
      res.status(400).json({ error: error.errors });
      return;
    }
    console.error(error);
    res.status(500).json({ error: 'Erro ao criar evento' });
  }
});

// Atualizar evento
eventosRouter.put('/:id', async (req: Request, res: Response) => {
  try {
    const { id } = req.params;
    const eventoData = eventoSchema.parse(req.body);

    // Verificar conflito de horário para o mesmo curso, ignorando o próprio evento
    const conflito = await knex('eventos')
      .where('curso_id', eventoData.curso_id)
      .andWhere('id', '!=', id)
      .andWhere(function () {
        this.whereBetween('data_inicio', [eventoData.data_inicio, eventoData.data_fim])
          .orWhereBetween('data_fim', [eventoData.data_inicio, eventoData.data_fim])
          .orWhereRaw('? BETWEEN data_inicio AND data_fim', [eventoData.data_inicio])
          .orWhereRaw('? BETWEEN data_inicio AND data_fim', [eventoData.data_fim]);
      })
      .first();

    if (conflito) {
      res.status(400).json({ error: 'Já existe um evento para este curso nesse período.' });
      return;
    }

    const [evento] = await knex('eventos')
      .where({ id })
      .update(eventoData)
      .returning('*');

    if (!evento) {
      res.status(404).json({ error: 'Evento não encontrado' });
      return;
    }

    res.json(evento);
  } catch (error) {
    if (error instanceof z.ZodError) {
      res.status(400).json({ error: error.errors });
      return;
    }
    console.error(error);
    res.status(500).json({ error: 'Erro ao atualizar evento' });
  }
});

// Deletar evento
eventosRouter.delete('/:id', async (req: Request, res: Response) => {
  try {
    const { id } = req.params;
    const deleted = await knex('eventos')
      .where({ id })
      .delete();

    if (!deleted) {
      res.status(404).json({ error: 'Evento não encontrado' });
      return;
    }

    res.status(204).send();
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Erro ao deletar evento' });
  }
});

export default eventosRouter; 