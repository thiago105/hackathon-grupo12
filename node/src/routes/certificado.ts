import express from 'express'
import knex from '../db/knex';
import PDFDocument from 'pdfkit';

const router = express.Router()

router.get('/:usuarioId/:eventoId', async (req, res) => {
  const { usuarioId, eventoId } = req.params

  try {
    const inscricao = await knex('inscricoes')
      .where({ usuario_id: usuarioId, evento_id: eventoId })
      .andWhere({ aprovado: true })
      .first()

    if (!inscricao) {
      res.status(403).json({ message: 'Usuário não aprovado para este evento.' })
    } else {
      const dados = await knex('inscricoes')
        .join('usuarios', 'usuarios.id', 'inscricoes.usuario_id')
        .join('eventos', 'eventos.id', 'inscricoes.evento_id')
        .join('palestrantes', 'palestrantes.id', 'eventos.palestrante_id')
        .join('cursos', 'cursos.id', 'usuarios.curso_id')
        .where('inscricoes.usuario_id', usuarioId)
        .andWhere('inscricoes.evento_id', eventoId)
        .select(
          'usuarios.nome as usuario_nome',
          'eventos.nome as evento_nome',
          'eventos.data_inicio',
          'eventos.endereco',
          'palestrantes.nome as palestrante_nome',
          'cursos.nome as curso_nome'
        ).first()

      const data = new Date(dados.data_inicio);
      const dataFormatada = `${String(data.getDate()).padStart(2, '0')}/${String(data.getMonth() + 1).padStart(2, '0')}/${data.getFullYear()}`;

      const doc = new PDFDocument()
      res.setHeader('Content-Type', 'application/pdf')
      res.setHeader('Content-Disposition', 'inline; filename=certificado.pdf')
      doc.pipe(res)

      doc.fontSize(20).text('CERTIFICADO', { align: 'center' })
      doc.moveDown()
      doc.fontSize(16).text('Parabéns pelo certificado!', { align: 'center' })
      doc.moveDown(2)
      doc.fontSize(14).text(`Participante: ${dados.usuario_nome}`)
      doc.text(`Evento: ${dados.evento_nome}`)
      doc.text(`Curso: ${dados.curso_nome}`)
      doc.text(`Palestrante: ${dados.palestrante_nome}`)
      doc.text(`Data: ${dataFormatada}`)
      doc.text(`Local do evento: ${dados.endereco}`)

      doc.end()
    }
  } catch (error) {
    console.error('Erro ao gerar certificado:', error)
    res.status(500).json({ message: 'Erro interno ao gerar certificado.' })
  }
})

export default router;