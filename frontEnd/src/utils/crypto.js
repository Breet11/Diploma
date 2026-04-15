import i18n from '../i18n';

const RSA_ALGORITHM = 'RSA-OAEP';
const HASH_ALGORITHM = 'SHA-256';
const PUBLIC_KEY_PEM = `-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA81rWKsOBDj8JSzgiPGDE
Fkipwrs26YD6VRNrGvstuO0V/oPZshaU4AzJe58W25EprBwqxpXjEpZOSpaAO9fR
A8Ofnh5Iet9PbltmrVxwRIHQn/SciOQiCnIIGlCTMqIKn+n4B/nIaivq9wpw9n9m
XNe2uTyNgAQtLzMzGnSJTj0oYr7AC0m4DD1GdFOxxNS41qIyN1Lx7K0mXVGa3C/X
lWLJEZgQhRNn7Af6lRqNWJFiU9yI0Q2jMfkghRJE4bsdl+TA0dr9pT7NNn4VGLyn
r+hiBY7xfKbZ8ABt/HthaXk1I+tXsimqWYIkGRUWcXeTXVabNP3iEIktRxjhhDLA
2QIDAQAB
-----END PUBLIC KEY-----`;

let cryptoKeyPromise = null;

export async function encryptAuthPassword(password) {
  if (!window.crypto?.subtle) {
    throw new Error(i18n.global.t('errors.browserCryptoUnsupported'));
  }

  const cryptoKey = await getCryptoKey();
  const encryptedBuffer = await window.crypto.subtle.encrypt(
    {
      name: RSA_ALGORITHM
    },
    cryptoKey,
    new TextEncoder().encode(password)
  );

  return arrayBufferToBase64(encryptedBuffer);
}

async function getCryptoKey() {
  if (!cryptoKeyPromise) {
    const publicKeyBinary = pemToArrayBuffer(PUBLIC_KEY_PEM);
    cryptoKeyPromise = window.crypto.subtle.importKey(
      'spki',
      publicKeyBinary,
      {
        name: RSA_ALGORITHM,
        hash: HASH_ALGORITHM
      },
      false,
      ['encrypt']
    );
  }

  return cryptoKeyPromise;
}

function pemToArrayBuffer(pem) {
  if (pem.includes('PASTE_YOUR_PUBLIC_RSA_KEY_HERE')) {
    throw new Error(i18n.global.t('errors.publicKeyMissing'));
  }

  const sanitizedPem = pem
    .replace('-----BEGIN PUBLIC KEY-----', '')
    .replace('-----END PUBLIC KEY-----', '')
    .replace(/\s/g, '');

  const binaryString = window.atob(sanitizedPem);
  const bytes = new Uint8Array(binaryString.length);

  for (let index = 0; index < binaryString.length; index += 1) {
    bytes[index] = binaryString.charCodeAt(index);
  }

  return bytes.buffer;
}

function arrayBufferToBase64(buffer) {
  const bytes = new Uint8Array(buffer);
  let binary = '';

  for (const byte of bytes) {
    binary += String.fromCharCode(byte);
  }

  return window.btoa(binary);
}


