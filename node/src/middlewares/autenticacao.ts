import {
  Request,
  Response,
  NextFunction//sem essa buxa aqui a requisição fica presa aqui
} from 'express'
import {
  verify
} from 'jsonwebtoken'

function autenticacao(
  req: Request,
  res: Response,
  next: NextFunction 
) {
  const authHeader = req.headers.authorization

  if (!authHeader) {
    res.status(401).json({ message: 'Token invalido!' })
    return;
  }

  // "Bearer lfjebwpuviberugivpbervuberpg.lkjvbrqeçerg"
  const [, token] = authHeader.split(" ");
  // essa virgula ignora o primeiro elemento que no caso é o bearer
  try {
    const dadosToken =
      verify(token, 'NAOPASSARNGM_lhfvqwkufvyk')

      console.log('dadosToken', dadosToken)

      next()
      return
  } catch (error) {
    res.status(401).json({message: "Token invalido"})
    return;
  }
}

export default autenticacao
