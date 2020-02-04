using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace MockAPIGateway.Controllers
{
    [Route("api/")]
    [ApiController]
    public class VehicleActionsController : ControllerBase
    {
        [HttpPost("useVehicle")]
        public ActionResult UseVehicle()
        {
            return Ok();
        }

        [HttpPost("releaseVehicle")]
        public ActionResult ReleaseVehicle()
        {
            return Ok();
        }

        [HttpPost("setTemperatureLimit")]
        public ActionResult SetTemperatureLimit()
        {
            return Ok();
        }

        [HttpPost("setHumidityLimit")]
        public ActionResult SetHumidityLimit()
        {
            return Ok();
        }

        [HttpPost("setLightIntensityLimit")]
        public ActionResult SetLightIntensityLimit()
        {
            return Ok();
        }
    }
}