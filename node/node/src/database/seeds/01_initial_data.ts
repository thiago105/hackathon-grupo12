import { Knex } from 'knex';
import bcrypt from 'bcrypt';

export async function seed(knex: Knex): Promise<void> {
  // Inserir cursos
  const cursos = await knex('cursos').insert([
    { nome: 'Ciência da Computação' },
    { nome: 'Engenharia de Software' },
    { nome: 'Sistemas de Informação' }
  ]).returning('id');

  // Inserir usuário administrador
  const senhaHash = await bcrypt.hash('admin123', 10);
  await knex('usuarios').insert({
    nome: 'Administrador',
    email: 'admin@unialfa.com.br',
    senha_hash: senhaHash,
    curso_id: cursos[0]
  });

  // Inserir palestrantes
  const palestrantes = await knex('palestrantes').insert([
    {
      nome: 'João Silva',
      mini_curriculo: 'Doutor em Ciência da Computação com 10 anos de experiência em desenvolvimento de software.',
      tema: 'Desenvolvimento Web Moderno',
      foto_url: 'https://example.com/foto1.jpg'
    },
    {
      nome: 'Maria Santos',
      mini_curriculo: 'Mestre em Engenharia de Software e especialista em Arquitetura de Software.',
      tema: 'Arquitetura de Software',
      foto_url: 'https://example.com/foto2.jpg'
    }
  ]).returning('id');

  // Inserir eventos
  await knex('eventos').insert([
    {
      nome: 'Semana de Tecnologia 2024',
      data_inicio: '2024-04-01',
      data_fim: '2024-04-05',
      hora: '19:00:00',
      endereco: 'Auditório Principal - Campus UniALFA',
      curso_id: cursos[0],
      palestrante_id: palestrantes[0]
    },
    {
      nome: 'Workshop de Arquitetura de Software',
      data_inicio: '2024-04-10',
      data_fim: '2024-04-10',
      hora: '14:00:00',
      endereco: 'Sala 101 - Bloco B',
      curso_id: cursos[1],
      palestrante_id: palestrantes[1]
    }
  ]);
} 