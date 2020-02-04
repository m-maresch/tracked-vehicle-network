using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace MockAPIGateway.Controllers
{
    [Route("api/")]
    [ApiController]
    public class SessionsController : ControllerBase
    {
        [HttpPost("login")]
        public ActionResult Login()
        {
            return Ok(new
            {
                Token = "<Token string>"
            });
        }

        [HttpPost("logout")]
        public ActionResult Logout()
        {
            return Ok();
        }
    }
}