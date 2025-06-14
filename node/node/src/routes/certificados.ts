import { Router, Request, Response } from 'express';
import PDFDocument from 'pdfkit';
import knex from '../database';

const certificadosRouter = Router();

certificadosRouter.get('/:inscricao_id', async (req: Request, res: Response) => {
  const { inscricao_id } = req.params;

  // Buscar dados da inscrição, usuário e evento
  const inscricao = await knex('inscricoes')
    .where({ id: inscricao_id })
    .first();

  if (!inscricao) {
    res.status(404).json({ error: 'Inscrição não encontrada' });
    return;
  }

  const usuario = await knex('usuarios').where({ id: inscricao.usuario_id }).first();
  const evento = await knex('eventos').where({ id: inscricao.evento_id }).first();

  if (!usuario || !evento) {
    res.status(404).json({ error: 'Usuário ou evento não encontrado' });
    return;
  }

  // Gerar PDF
  const doc = new PDFDocument({ size: 'A4', margin: 50 });
  res.setHeader('Content-Type', 'application/pdf');
  res.setHeader('Content-Disposition', `attachment; filename=certificado_${usuario.nome}.pdf`);
  doc.pipe(res);

  doc.fontSize(24).text('Certificado de Participação', { align: 'center' });
  doc.moveDown(2);
  doc.fontSize(16).text(
    `Certificamos que ${usuario.nome} participou do evento "${evento.nome}" realizado em ${evento.data_inicio} no endereço ${evento.endereco}.`,
    { align: 'center' }
  );
  doc.moveDown(2);
  doc.fontSize(12).text('Faculdade UniALFA - Umuarama', { align: 'center' });
  doc.end();
});

export default certificadosRouter; 